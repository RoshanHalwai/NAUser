package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class AddDailyServiceAndFamilyMembers extends BaseActivity {

    private final int RESULT_PICK_CONTACT = 1;
    private final int CAMERA_REQUEST = 2;
    private final int GALLERY_REQUEST = 3;
    private TextView textDescriptionDailyService;
    private TextView textDescriptionFamilyMember;
    private EditText editPickTime;
    private EditText editDailyServiceOrFamilyMemberName;
    private EditText editDailyServiceOrFamilyMemberMobile;
    private Button buttonAdd;
    private CircleImageView circleImageView;
    private String selectedTime;
    private String service_type;
    private AlertDialog dialog;
    private ListView listView;

    private boolean grantedAccess = false;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_daily_service_and_family_members;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Add Daily Service and Add Family Members, we set the title
         * based on the user click on MySweetHome screen and on click of listview on MyDailyServices*/
        if (getIntent().getIntExtra(Constants.SCREEN_TYPE, 0) == R.string.my_sweet_home) {
            return R.string.add_family_members_details_screen;
        } else {
            return R.string.add_my_service;
        }
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
        TextView textDailyServiceOrFamilyMemberName = findViewById(R.id.textDailyServiceOrFamilyMemberName);
        TextView textDailyServiceOrFamilyMemberMobile = findViewById(R.id.textDailyServiceOrFamilyMemberMobile);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        TextView textRelation = findViewById(R.id.textFamilyMemberRelation);
        TextView textOr = findViewById(R.id.textOr);
        TextView textTime = findViewById(R.id.textTime);
        TextView textGrantAccess = findViewById(R.id.textGrantAccess);
        textDescriptionDailyService = findViewById(R.id.textDescriptionDailyService);
        textDescriptionFamilyMember = findViewById(R.id.textDescriptionFamilyMember);
        editDailyServiceOrFamilyMemberName = findViewById(R.id.editDailyServiceOrFamilyMemberName);
        editDailyServiceOrFamilyMemberMobile = findViewById(R.id.editDailyServiceOrFamilyMemberMobile);
        EditText editFamilyMemberRelation = findViewById(R.id.editFamilyMemberRelation);
        editPickTime = findViewById(R.id.editPickTime);
        Button buttonSelectFromContact = findViewById(R.id.buttonSelectFromContact);
        Button buttonYes = findViewById(R.id.buttonYes);
        Button buttonNo = findViewById(R.id.buttonNo);
        buttonAdd = findViewById(R.id.buttonAdd);
        circleImageView = findViewById(R.id.dailyServiceOrFamilyMemberProfilePic);
        listView = listAddImageServices.findViewById(R.id.listAddImageService);

        /*Setting font for all the views*/
        textDailyServiceOrFamilyMemberName.setTypeface(Constants.setLatoBoldFont(this));
        textDailyServiceOrFamilyMemberMobile.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoItalicFont(this));
        textRelation.setTypeface(Constants.setLatoBoldFont(this));
        textOr.setTypeface(Constants.setLatoBoldFont(this));
        textTime.setTypeface(Constants.setLatoBoldFont(this));
        textGrantAccess.setTypeface(Constants.setLatoBoldFont(this));
        textDescriptionDailyService.setTypeface(Constants.setLatoBoldFont(this));
        textDescriptionFamilyMember.setTypeface(Constants.setLatoBoldFont(this));
        editDailyServiceOrFamilyMemberName.setTypeface(Constants.setLatoRegularFont(this));
        editDailyServiceOrFamilyMemberMobile.setTypeface(Constants.setLatoRegularFont(this));
        editFamilyMemberRelation.setTypeface(Constants.setLatoRegularFont(this));
        editPickTime.setTypeface(Constants.setLatoRegularFont(this));
        buttonSelectFromContact.setTypeface(Constants.setLatoLightFont(this));
        buttonYes.setTypeface(Constants.setLatoRegularFont(this));
        buttonNo.setTypeface(Constants.setLatoRegularFont(this));
        buttonAdd.setTypeface(Constants.setLatoLightFont(this));

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickTime.setInputType(InputType.TYPE_NULL);

        /*Setting event for circleImageView*/
        circleImageView.setOnClickListener(v -> dialog.show());

        /*Setting event for change in editDailyServicesOrFamilyMemberName*/
        //setEditDailyServiceOrFamilyMemberName();

        /*Since we are using same layout for add my daily services and and add my family members we need to show different layout
          according to the user choice*/
        if (getIntent().getIntExtra(Constants.SCREEN_TYPE, 0) == R.string.my_daily_services) {
            RelativeLayout relativeLayoutTime = findViewById(R.id.relativeLayoutTime);
            relativeLayoutTime.setVisibility(View.VISIBLE);
            textDescriptionFamilyMember.setVisibility(View.GONE);
            updateOTPDescription();
        } else {
            LinearLayout layoutAccess = findViewById(R.id.layoutAccess);
            textDescriptionFamilyMember.setVisibility(View.VISIBLE);
            textRelation.setVisibility(View.VISIBLE);
            editFamilyMemberRelation.setVisibility(View.VISIBLE);
            layoutAccess.setVisibility(View.VISIBLE);
            buttonAdd.setVisibility(View.VISIBLE);
        }

        /*Setting event for Select From Contacts button*/
        buttonSelectFromContact.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(i, RESULT_PICK_CONTACT);
        });

        /*Method for button Yes*/
        buttonYes.setOnClickListener(v -> {
            //setEditDailyServiceOrFamilyMemberName();
            grantedAccess = true;
            buttonYes.setBackgroundResource(R.drawable.button_guest_selected);
            buttonNo.setBackgroundResource(R.drawable.button_guest_not_selected);
            buttonYes.setTextColor(Color.WHITE);
            buttonNo.setTextColor(Color.BLACK);
        });

        /*Method for button No*/
        buttonNo.setOnClickListener(v -> {
            grantedAccess = false;
            buttonYes.setBackgroundResource(R.drawable.button_guest_not_selected);
            buttonNo.setBackgroundResource(R.drawable.button_guest_selected);
            buttonYes.setTextColor(Color.BLACK);
            buttonNo.setTextColor(Color.WHITE);
        });

        /*Setting event for  Displaying Date & Time*/
        editPickTime.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                displayTime();
            }
        });
        editPickTime.setOnClickListener(View -> displayTime());

        /*Setting events for add button click*/
        buttonAdd.setOnClickListener(v -> {
            if (getIntent().getIntExtra(Constants.SCREEN_TYPE, 0) == R.string.my_daily_services) {
                Intent intentButtonAdd = new Intent(AddDailyServiceAndFamilyMembers.this, OTP.class);
                intentButtonAdd.putExtra(Constants.OTP_TYPE, service_type);
                intentButtonAdd.putExtra(Constants.SCREEN_TYPE, R.string.add_my_service);
                startActivity(intentButtonAdd);
            } else {
                if (grantedAccess)
                    openNotificationDialog();
                else {
                    Intent intentNotification = new Intent(AddDailyServiceAndFamilyMembers.this, OTP.class);
                    intentNotification.putExtra(Constants.OTP_TYPE, "Family Member");
                    startActivity(intentNotification);
                }
            }
        });
        setupViewsForProfilePhoto();
    }

    private void openNotificationDialog() {
        AlertDialog.Builder alertNotificationDialog = new AlertDialog.Builder(this);
        TextView textNotificationTitle = new TextView(this);

        //Setting Custom Dialog Title
        textNotificationTitle.setText(R.string.notification_dialog_box);
        //Title Properties
        textNotificationTitle.setPadding(10, 10, 10, 10);
        textNotificationTitle.setGravity(Gravity.CENTER);
        textNotificationTitle.setTextColor(getResources().getColor(R.color.nmLightYellow));
        textNotificationTitle.setTextSize(R.dimen.text_view_default_size);
        textNotificationTitle.setBackgroundColor(getResources().getColor(R.color.nmBlack));
        textNotificationTitle.setTypeface(Constants.setLatoBoldFont(this));
        alertNotificationDialog.setTitle(R.string.notification_dialog_box);

        //Setting ImageView
        ImageView imageNotification = new ImageView(this);
        imageNotification.setImageResource(R.drawable.feature_unavailable);
        alertNotificationDialog.setIcon(R.drawable.feature_unavailable);

        TextView textNotificationMessage = new TextView(this);
        // Setting Custom Dialog Message
        textNotificationMessage.setText(R.string.access_to_notifications);
        // Message Properties
        textNotificationMessage.setPadding(10, 10, 10, 10);
        textNotificationMessage.setTextColor(Color.BLACK);
        textNotificationMessage.setTypeface(Constants.setLatoBoldFont(this));
        textNotificationMessage.setGravity(Gravity.CENTER);
        //textNotificationMessage.setTextSize(R.dimen.text_view_default_size);
        alertNotificationDialog.setView(textNotificationMessage);

        // Setting Custom Dialog Buttons
        alertNotificationDialog.setPositiveButton("Accept", (dialog, which) -> {
            Intent intentNotification = new Intent(AddDailyServiceAndFamilyMembers.this, OTP.class);
            intentNotification.putExtra(Constants.OTP_TYPE, "Family Member");
            startActivity(intentNotification);
        });
        alertNotificationDialog.setNegativeButton("Reject", (dialog, which) -> dialog.cancel());

        new Dialog(getApplicationContext());
        alertNotificationDialog.show();
    }


    /*This method will get invoked when user tries to enter his name*/
/*    private void setEditDailyServiceOrFamilyMemberName() {
        dailyServiceOrFamilyMemberName = editDailyServiceOrFamilyMemberName.getText().toString().trim();
        if (getIntent().getIntExtra(Constants.SCREEN_TYPE, 0) == R.string.my_sweet_home) {
            String description = getResources().getString(R.string.otp_message_family_member).replace("name", dailyServiceOrFamilyMemberName);
            textDescriptionFamilyMember.setText(description);
        }
    }*/

    /**
     * We need to update OTP Message description based on Service for which user is entering the
     * details.
     */
    private void updateOTPDescription() {
        if (getIntent().getExtras() != null) {
            service_type = getIntent().getStringExtra(Constants.SERVICE_TYPE);
            String description = getResources().getString(R.string.send_otp_message).replace("visitor", service_type);
            textDescriptionDailyService.setText(description);
        }
    }

    private void setupViewsForProfilePhoto() {
        /*Creating an array list of selecting images from camera and gallery*/
        ArrayList<String> pickImageList = new ArrayList<>();

        /*Adding pick images services to the list*/
        pickImageList.add(getString(R.string.camera));
        pickImageList.add(getString(R.string.gallery));
        pickImageList.add(getString(R.string.cancel));

        /*Creating the Adapter*/
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddDailyServiceAndFamilyMembers.this, android.R.layout.simple_list_item_1, pickImageList);

        /*Attaching adapter to the listView*/
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
                                editDailyServiceOrFamilyMemberName.setText(name);
                                editDailyServiceOrFamilyMemberMobile.setText(formattedPhoneNumber);
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
                    textDescriptionDailyService.setVisibility(View.VISIBLE);
                    buttonAdd.setVisibility(View.VISIBLE);
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }
}
