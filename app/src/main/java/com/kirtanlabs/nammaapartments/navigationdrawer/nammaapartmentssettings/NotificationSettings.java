package com.kirtanlabs.nammaapartments.navigationdrawer.nammaapartmentssettings;

import android.os.Bundle;
import android.widget.Switch;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

public class NotificationSettings extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_notification_settings;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.notifications;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        Switch switchNewMessage = findViewById(R.id.switchNewMessage);
        Switch switchEmailNotification = findViewById(R.id.switchEmailNotification);
        Switch switchVibrate = findViewById(R.id.switchVibrate);
        Switch switchSound = findViewById(R.id.switchSound);
        Switch switchProductUpdate = findViewById(R.id.switchProductUpdate);

        /*Setting font for all the views*/
        switchNewMessage.setTypeface(Constants.setLatoBoldFont(this));
        switchEmailNotification.setTypeface(Constants.setLatoBoldFont(this));
        switchVibrate.setTypeface(Constants.setLatoBoldFont(this));
        switchSound.setTypeface(Constants.setLatoBoldFont(this));
        switchProductUpdate.setTypeface(Constants.setLatoBoldFont(this));
    }
}
